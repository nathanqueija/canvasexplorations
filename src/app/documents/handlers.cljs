(ns app.documents.handlers
  (:require [app.documents.core :as documents.core]
            [re-frame.core :as rf]))

(rf/reg-event-fx
 :documents/fetch-started
 (fn [{:keys [db]} _]
   {:db (documents.core/fetch-started db)}))

(rf/reg-event-fx
 :documents/fetch-done
 (fn [{:keys [db]} [_ response]]
   {:db (documents.core/fetch-done db response)}))

(rf/reg-event-fx
 :documents/fetch-failed
 (fn [{:keys [db]} [_ {:keys [error]}]]
   {:db (documents.core/fetch-failed db error)}))
