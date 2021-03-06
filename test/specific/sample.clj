(ns specific.sample
  (:require [clojure.spec]
            [clojure.java.shell :as shell]
            [clojure.string :as string]))

(defn greet [pre sufs]
  (string/join ", " (cons pre sufs)))

(defn cowsay [msg]
  (shell/sh "cowsay" msg)) ; Fails in some environments

(defn some-fun [greeting & names]
  (:out (cowsay (greet greeting names))))

(clojure.spec/def ::exit (clojure.spec/and integer? #(>= % 0) #(< % 256)))
(clojure.spec/def ::out string?)
(clojure.spec/def ::fun-greeting string?)
(clojure.spec/fdef greet :ret ::fun-greeting)
(clojure.spec/fdef cowsay
                   :args (clojure.spec/cat :fun-greeting ::fun-greeting)
                   :ret (clojure.spec/keys :req-un [::out ::exit]))
(clojure.spec/fdef some-fun
                   :args (clojure.spec/cat :greeting ::fun-greeting
                                           :names (clojure.spec/* string?))
                   :ret string?)
