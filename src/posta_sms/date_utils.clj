(ns posta-sms.date-utils
  (:require [clj-time.core :as t]
            [clj-time.format :as f]))

(defn unparse-date
  [date]
  (f/unparse (f/formatter :mysql (t/time-zone-for-id "America/Montevideo")) date))