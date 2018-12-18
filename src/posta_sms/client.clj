(ns posta-sms.client
  (:require [clj-http.client :as http-client]
            [cheshire.core :refer [generate-string parse-string]]
            [posta-sms.logger :as log]))

(def base-url "http://test.smartnotifications.com.uy/api/v1")

(defrecord Client [companyId])

(defn- url-send
  []
  (format "%s/sms" base-url))

(defn- url-check-status
  [sms-request-id]
  (format "%s/request/%s/sms/status" base-url sms-request-id))

(defn- request
  ([method url]
   (request method url {}))
  ([method url params]
   (let [params (merge {:headers      {"User-Agent" "posta-sms/0.1.0"}
                        :as           :json
                        :content-type :json
                        :method       method
                        :url          url}
                       (if (= method :get)
                         {:query-params params}
                         {:body (generate-string params)}))]
     (try
       (let [{:keys [status body]} (http-client/request params)]
         {:status status :body body})
       (catch Exception e
         (let [{:keys [status body]} (ex-data e)
               body (if (= status 401)
                      body
                      (merge (-> (:body params)
                                 (parse-string true)
                                 (dissoc :text))
                             (parse-string body true)))]
           (log/error (format "Status: %s - %s" status body))
           {:status status :body body}))))))

(defn create
  [company-id]
  (->Client company-id))

(defn send-sms!
  [client messages]
  (request :post (url-send) (assoc client :messages messages)))

(defn check-sms-status
  [sms-request-id]
  (request :get (url-check-status sms-request-id)))