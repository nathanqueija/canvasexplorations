(ns app.shapes.core
  (:require [applied-science.js-interop :as j]))

(defmulti is-hitting-mouse? :shape-type)

(defmethod is-hitting-mouse? :rect
  [{:keys [x y width height] :as shape} mouse-pos]
  (when
   (and
    (> (:y mouse-pos)
       y)
    (< (:y mouse-pos)
       (+ y height))
    (> (:x mouse-pos)
       x)
    (< (:x mouse-pos)
       (+ x  width)))
    shape))

(defmethod is-hitting-mouse? :circle
  [shape mouse-pos]
  ;; Pythagoras' Theorem: calculating if the hypothenuse
  ;; from the triangle formed between the center of the shape
  ;; and the mouse position is smaller than the circle radius
  (let [cos (- (:x shape) (:x mouse-pos))
        sin (- (:y shape) (:y mouse-pos))
        hypothenuse (js/Math.sqrt
                     (+ (js/Math.pow cos 2)
                        (js/Math.pow sin 2)))]
    (when (< hypothenuse (:radius shape))
      shape)))

(defmulti bounding-box :shape-type)

(defmethod bounding-box :rect
  [{:keys [x y width height]}]
  ;; TODO: calculate bounding box when there are transformations
  ;; applied to the shape (rotation, skew, etc.)
  {:top-left {:x x :y y}
   :top-right {:x (+ x width) :y y}
   :bottom-left {:x x :y (+ y height)}})

(defmethod bounding-box :circle
  [{:keys [x y radius]}]
  {:top-left {:x (- x radius) :y (- y radius)}
   :top-right {:x (+ x radius) :y (- y radius)}
   :bottom-left {:x (- x radius) :y (+ y radius)}})

(defn selection
  [{:keys [document selected-group ctx]}]
  (let [document-indexed-by-group (group-by :group document)
        group (get document-indexed-by-group selected-group)
        bounding-boxes (mapv #(bounding-box %) group)
        left (apply min-key #(get-in % [:top-left :x]) bounding-boxes)
        right (apply max-key #(get-in % [:top-right :x]) bounding-boxes)
        top (apply min-key #(get-in % [:top-left :y]) bounding-boxes)
        bottom (apply max-key #(get-in % [:bottom-left :y]) bounding-boxes)]
    (j/assoc! ctx :strokeStyle "#902fe0")
    (j/assoc! ctx :lineWidth 2)
    (.beginPath ctx)
    (.rect ctx
           (- (get-in left [:top-left :x]) 4)
           (- (get-in top [:top-right :y]) 4)
           (+ (- (get-in right [:top-right :x])
                 (get-in left [:top-left :x]))
              8)
           (+ (- (get-in bottom [:bottom-left :y])
                 (get-in top [:top-left :y]))
              8))
    (.stroke ctx)))



