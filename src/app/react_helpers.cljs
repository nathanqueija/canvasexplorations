(ns app.react-helpers
  (:refer-clojure :exclude [deref])
  (:require ["react" :as react]
            ["react-dom" :as react-dom]))

(defn- array-deps [deps]
  (if (or (array? deps) (nil? deps)) deps (into-array deps)))

(def create-element react/createElement)

(def clone-element react/cloneElement)

(defn deref
  [^js ref]
  (.-current ref))

(defn deref-element
  "Useful when one expects `ref` to be an HTMLElement or a Ref to an HTMLElement.

  Accepts a default HTMLElement (or Ref) that gets returned when the underlying element is nil."
  ([^js ref]
   (deref-element ref nil))
  ([^js ref default]
   (or (when ref
         (if (instance? js/HTMLElement ref)
           ref
           (deref ref)))
       (some-> default (deref-element nil)))))

(defn set-ref! [^js ref value]
  (set! (.-current ref) value))

(defn swap-ref! [^js ref f & args]
  (set-ref! ref (apply f (deref ref) args)))

(def use-ref react/useRef)

(def forward-ref react/forwardRef)

;; TODO: Maybe we can simply alias
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

(defn use-layout-effect
  ([setup-fn]
   (react/useLayoutEffect (make-effect-fn setup-fn)))
  ([setup-fn deps]
   (react/useLayoutEffect (make-effect-fn setup-fn) (array-deps deps))))

(defn use-imperative-handle
  ([ref setup-fn]
   (react/useImperativeHandle ref setup-fn))
  ([ref setup-fn deps]
   (react/useImperativeHandle ref setup-fn (array-deps deps))))

(defn use-callback
  ([callback]
   (react/useCallback callback))
  ([callback deps]
   (react/useCallback callback (array-deps deps))))

(defn use-memo
  ([f]
   (react/useMemo f))
  ([f deps]
   (react/useMemo f (array-deps deps))))

(def use-context react/useContext)

(def create-context react/createContext)

(defn provide-context
  "Provide a `value` to consumers in UI subtree via React’s Context API."
  [^js context value & children]
  (apply react/createElement (.-Provider context) #js {:value value} children))

(defn with-context
  "Subscribes UI subtree to context changes. Calls `render-child` everytime
  a new value gets added into context via `Provider`."
  [^js ctx render-child]
  (react/createElement (.-Consumer ctx) nil #(render-child %)))

(def create-portal react-dom/createPortal)

(def Fragment react/Fragment)

(def valid-element? react/isValidElement)

(defn fragment-element?
  "Test if a react element is a fragment."
  [element]
  (= (or (.-type element) element) react/Fragment))

(defn only-child!
  "Check that `children` have only one element and return it, otherwise throw."
  [children]
  (react/Children.only children))

(defn children
  "Return the (maybe render-prop) children from `props`."
  [^js props & args]
  (let [ch (.-children props)]
    (if (fn? ch) (apply ch args) ch)))
