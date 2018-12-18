# posta-sms

A Clojure library designed to send SMS messages through **posta SMS API Platfotm**.


## Installation

To install, add the following dependency to your project.clj file:

```
[posta-sms "0.1.0"]
```

## Usage

To get started with using posta-sms, require posta-sms.client and posta-sms.message in your project:

```
(ns postasms.sample
       (:require [posta-sms.client :as sms-client]
                 [posta-sms.message :as sms-message]))
```

#### Create your client

```
(def client (sms-client/create "YOUR COMPANY ID"))
```

#### Send SMS

```
(sms-client/send-sms! client messages)
```

**Note**

*messages are created this way*:

```
(sms-message/create (:number %) (:text %) (:id %) (:schedule %))
```

#### Check SMS status

```
(sms-client/check-sms-status (:smsRequestId send-sms-request))
```

**Parameters**

*smsRequestId*: this ID comes in the response to the sending sms request.

## License

Copyright © 2018 Alejandro Winkler

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
