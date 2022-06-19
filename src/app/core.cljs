(ns app.core
  (:require app.handlers
            app.fx
            app.subs
            [components.app.views :as app]
            [devtools.core :as devtools]
            [reagent.core :as r]
            [re-frame.core :as rf]
            ["react-dom/client" :as react-dom]))

(devtools/install!)

(defonce dom-container (.getElementById js/document "app"))

(defonce root (.createRoot react-dom dom-container))

(defn render
  []
  (.render root (r/as-element [app/base])))

(defn start
  []
  (rf/dispatch [:app/launched])
  (render))
