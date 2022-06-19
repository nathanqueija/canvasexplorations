(ns app.documents.core
  (:require [clojure.edn :as edn]))

(defonce document-url "/data/shapes.edn")

(defn fetch-started [db]
  (assoc-in db [:documents :status] :loading))

(defn fetch-done
  [db {:keys [document]}]
  (-> db
      (assoc-in [:documents :status] :loaded)
      (assoc-in [:documents :current-document] document)))

(defn fetch-failed [db {:keys [error]}]
  (-> db
      (assoc-in [:documents :status] :error)
      (assoc-in [:documents :error] error)))

(defn status [db]
  (get-in db [:documents :status]))

(defn error [db]
  (get-in db [:documents :error]))

(defn current [db]
  (get-in db [:documents :current-document]))

(defn fetch-document+
  []
  (-> (js/fetch document-url)
      (.then #(.text %))
      (.then #(edn/read-string %))))
