(ns app.react-helpers
  (:require ["react" :as react]))

(defn- array-deps [deps]
  (if (or (array? deps) (nil? deps)) deps (into-array deps)))

(def use-ref react/useRef)

(defn use-state
  ([]
   (react/useState))
  ([initial-state]
   (react/useState initial-state)))

(defn- make-effect-fn
  [f]
  (fn []
    (let [res (f)]
      (if (fn? res) res js/undefined))))

(defn use-effect
  ([setup-fn]
   (react/useEffect (make-effect-fn setup-fn)))
  ([setup-fn deps]
   (react/useEffect (make-effect-fn setup-fn) (array-deps deps))))

