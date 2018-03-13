;; grids based on noise, result used in pix2line glitch sketch

(ns examples.ex17-grids
  (:require [clojure2d.core :refer :all]
            [fastmath.core :as m]
            [fastmath.random :as r]
            [clojure2d.pixels :as p]
            [clojure2d.color :as c])
  (:import [clojure2d.pixels Pixels]))

(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
(m/use-primitive-operators)

(def cnvs (canvas 600 600 :low))

(def window (show-window cnvs "grid" 10 nil))

(defmethod key-pressed ["grid" \space] [_ _]
  (save cnvs (next-filename "results/ex17/" ".jpg")))

(def dark (c/awt-color 2 22 52))
(def light (c/awt-color 232 221 203))

(defn draw-grid
  ""
  [canvas]
  (let [nx (r/irand 2 200)
        ny (r/irand 2 200)
        noise (r/make-random-noise-fn)
        scale (r/irand 5)
        nnx (inc (m/round (* scale nx)))
        nny (inc (m/round (* scale ny)))
        div (* (- 201 nx) (- 201 ny))
        shift (r/drand -10 10)]
    (println (str "nx=" nx))
    (println (str "ny=" ny))
    (dotimes [y 600]
      (dotimes [x 600]
        (let [time (+ shift (int (/ (+ x (* y 600)) div)))
              yy (* (quot x nnx) (int (* nnx (inc ^double (noise (quot x nnx) time )))))
              xx (* (quot y nny) (int (* nny (inc ^double (noise (quot y nny) (- time))))))
              n (< ^double (noise (quot (+ x xx) nx) (quot (+ y yy) ny)) 0.5)]
          (set-awt-color canvas (if n dark light))
          (rect canvas x y 1 1))))))

(do
  (with-canvas-> cnvs
    (draw-grid))
  :done)
