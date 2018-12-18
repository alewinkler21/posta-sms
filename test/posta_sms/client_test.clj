(ns posta-sms.client-test
  (:require [clojure.test :refer :all]
            [posta-sms.client :as sms-client]
            [posta-sms.message :as sms-message]
            [clj-time.core :as t]
            [posta-sms.logger :as log]))

(def test-data (#(letfn [(uuid [] (str (java.util.UUID/randomUUID)))]
                   (let [current-date (t/now)]
                     [{:number   "091335155"
                       :text     "this is the text 1"
                       :id       (uuid)
                       :schedule current-date}
                      {:number   "099694853"
                       :text     "this is the text 2"
                       :id       (uuid)
                       :schedule current-date}]))))

(deftest test-send-sms!
  (testing "Testing send-sms!"
    (letfn [(successful-response? [response]
              (some #(= (:status response) %) '[200 201 202 204]))]
      (let [queued-sms (atom '())
            client (sms-client/create "1111")
            messages (map #(sms-message/create (:number %) (:text %) (:id %) (:schedule %)) test-data)
            send-sms-response (sms-client/send-sms! client messages)
            _ (log/info send-sms-response)]
        (when (successful-response? send-sms-response)
          (swap! queued-sms #(conj % (:body send-sms-response))))
        (let [send-sms-request (first @queued-sms)
              check-status-response (sms-client/check-sms-status (:smsRequestId send-sms-request))
              count-messages (->> (get-in check-status-response [:body :data])
                                  (count))
              _ (log/info check-status-response)]
          (is (= count-messages 2)))))))