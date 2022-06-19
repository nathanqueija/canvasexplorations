(ns app.canvas.core
  (:require [applied-science.js-interop :as j]))

(defn clear [{:keys [canvas ctx]}]
  ^js (.clearRect ctx 0 0 (.-width canvas) (.-height canvas))
  (.rect ctx 0 0 (.-width canvas) (.-height canvas))
  (j/assoc! ctx :fillStyle "#edf1f5")
  (.fill ctx))

(defn resize [{:keys [canvas ctx]}]
  (let [bbox (.getBoundingClientRect canvas)
        scale js/window.devicePixelRatio]
    (j/assoc! canvas :width (* (.-width bbox) scale))
    (j/assoc! canvas :height (* (.-height bbox) scale))
    (.scale ctx scale scale)))

