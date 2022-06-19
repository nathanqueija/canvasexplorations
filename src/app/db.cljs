(ns app.db
  (:require
   [app.documents.db :as documents]))

(def app-db (merge documents/db))
