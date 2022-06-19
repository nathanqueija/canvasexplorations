(ns components.canvas.context
  (:require [app.react-helpers :as rh]
            [reagent.core :as r]))

(defonce context (rh/create-context {:ctx nil
                                     :canvas nil}))
(defn provider
  [value & children]
  (rh/provide-context
   context
   value
   (r/as-element (into [:<>] children))))

(defn use-canvas []
  (rh/use-context context))
