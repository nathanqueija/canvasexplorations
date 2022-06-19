(ns components.canvas.views
  (:require
   [app.canvas.core :as canvas.core]
   [app.shapes.core :as shapes.core]
   [app.react-helpers :as rh]
   [components.shape.views :as shape.views]))

(defonce canvas-id "board")

(defn base
  [document]
  (let [!canvas (rh/use-ref nil)
        [ctx set-ctx!] (rh/use-state nil)
        [selected-group set-selecteg-group!] (rh/use-state nil)
        on-click (fn [ev]
                   (let [shape (loop [shapes (reverse document)]
                                 (if-let [shape (shapes.core/is-hitting-mouse?
                                                 (first shapes)
                                                 {:x (- (.-pageX ev) (.-offsetLeft (.-current !canvas)))
                                                  :y (- (.-pageY ev) (.-offsetTop (.-current !canvas)))})]
                                   shape
                                   (when-not (empty? (rest shapes))
                                     (recur (rest shapes)))))]
                     (set-selecteg-group! (:group shape))))

        draw (fn [opts]
               (canvas.core/clear opts)
               (doseq [shape document]
                 (shape.views/base shape opts))

               (when selected-group
                 ;; TODO: Optimise these calculations by indexing the shapes
                 ;; per group and calculating the bounding boxes only once
                 (shapes.core/selection {:document document
                                         :ctx ctx
                                         :selected-group selected-group})))]

    (rh/use-effect
     (fn []
       (let [canvas (.-current !canvas)
             ctx (.getContext canvas "2d")]
         (set-ctx! ctx)
         (canvas.core/resize {:ctx ctx
                              :canvas canvas})))
     [])

    (rh/use-effect
     (fn []
       (when ctx
         (draw {:ctx ctx
                :canvas (.-current !canvas)})))
     [selected-group ctx])

    [:canvas {:id canvas-id
              :ref !canvas
              :on-click on-click}]))
