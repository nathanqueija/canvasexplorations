(ns components.shape.views
  (:require [applied-science.js-interop :as j]
            [components.canvas.context :as canvas.context]))

(defmulti shape* :shape-type)

(defmethod shape* :rect
  [{:keys [fill x y width height]} {:keys [ctx]}]
  (-> ctx
      (j/assoc! :fillStyle fill)
      (.fillRect x y width height)))

(defmethod shape* :circle
  [{:keys [fill x y radius]} {:keys [ctx]}]
  (j/assoc! ctx :fillStyle fill)
  (.beginPath ctx)
  (.arc ctx x y radius 0 (* 2 js/Math.PI))
  (.fill ctx))

(defn base
  [shape]
  (let [react-context (canvas.context/use-canvas)]
    [shape* shape react-context]))
