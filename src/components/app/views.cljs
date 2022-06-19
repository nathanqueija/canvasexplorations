(ns components.app.views
  (:require [components.canvas.views :as canvas.views]
            [re-frame.core :as rf]))

:documents/current

(defn base []
  (let [status @(rf/subscribe [:documents/status])
        document @(rf/subscribe [:documents/current])]
    (case status
      :idle [:p "Welcome!"]
      :loading [:p "Loading document..."]
      :loaded [:f> canvas.views/base document]
      :error [:p "An error has happened. Please refresh the page."])))
