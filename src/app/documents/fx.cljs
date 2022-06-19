(ns app.documents.fx
  (:require [app.documents.core :as documents.core]
            [re-frame.core :as rf]))

(rf/reg-fx
 :documents/fetch
 (fn []
   (rf/dispatch [:documents/fetch-started])
   (-> (documents.core/fetch-document+)
       (.then (fn [document]
                (rf/dispatch [:documents/fetch-done {:document document}])))
       (.catch (fn [error]
                 (rf/dispatch [:documents/fetch-failed {:error error}]))))))
