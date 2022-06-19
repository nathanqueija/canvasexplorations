(ns components.canvas.views
  (:require
   [app.canvas.core :as canvas.core]
   [app.shapes.core :as shapes.core]
   [app.react-helpers :as rh]
   [components.canvas.context :as canvas.context]
   [components.shape.views :as shape.views]))

(defonce canvas-id "board")

(defn base
  [document]
  (let [[canvas set-canvas!] (rh/use-state nil)
        [ctx set-ctx!] (rh/use-state nil)
        [selected-group set-selecteg-group!] (rh/use-state nil)
        on-click (fn [ev]
                   (let [shape (loop [shapes (reverse document)]
                                 (if-let [shape (shapes.core/is-hitting-mouse?
                                                 (first shapes)
                                                 {:x (- (.-pageX ev) (.-offsetLeft canvas))
                                                  :y (- (.-pageY ev) (.-offsetTop canvas))})]
                                   shape
                                   (when-not (empty? (rest shapes))
                                     (recur (rest shapes)))))]
                     (set-selecteg-group! (:group shape))))

        draw (fn [opts]
               (canvas.core/clear opts)
              ;;  (doseq [shape document]
              ;;    (shape.views/base shape opts))

              ;;  (when selected-group
              ;;    ;; TODO: Optimise these calculations by indexing the shapes
              ;;    ;; per group and calculating the bounding boxes only once
              ;;    (shapes.core/selection {:document document
              ;;                            :ctx ctx
              ;;                            :selected-group selected-group}))
               )
        react-context {:ctx ctx
                       :canvas canvas}]

    (rh/use-effect
     (fn []
       (when (and ctx canvas)
         (canvas.core/resize {:ctx ctx
                              :canvas canvas})
        ;;  (canvas.core/clear {:ctx ctx
        ;;                      :canvas canvas})
         ))
     [ctx canvas])

    ;; (rh/use-effect
    ;;  (fn []
    ;;    (when ctx
    ;;      (draw {:ctx ctx
    ;;             :canvas (.-current !canvas)})))
    ;;  [selected-group ctx])

    [:canvas {:id canvas-id
              :ref (fn [ref]
                     (when ref
                       (set-canvas! ref)
                       (set-ctx! (.getContext ref "2d"))


                       (-> (.getContext ref "2d")
                          ;;  (j/assoc! :fillStyle fill)
                           (.fillRect 10 10 100 50))))
              :on-click on-click}
     [canvas.context/provider
      react-context
      (when
       (and canvas ctx)
        (map
         (fn [shape]
           ^{:key (random-uuid)}
           [:f> shape.views/base shape])
         document))]]))
