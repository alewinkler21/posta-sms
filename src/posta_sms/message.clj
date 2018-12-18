(ns posta-sms.message
  (:require [posta-sms.date-utils :as date-utils]))

(defrecord Message [phone text uniqueid schedule])

(defn create
  [phone text uniqueid schedule]
  (->Message phone text uniqueid (date-utils/unparse-date schedule)))