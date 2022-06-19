(ns app.documents.subs
  (:require [app.documents.core :as documents.core]
            [re-frame.core :as rf]))


(rf/reg-sub
 :documents/status
 (fn [db _]
   (documents.core/status db)))

(rf/reg-sub
 :documents/error
 (fn [db _]
   (documents.core/error db)))

(rf/reg-sub
 :documents/current
 (fn [db _]
   (documents.core/current db)))
