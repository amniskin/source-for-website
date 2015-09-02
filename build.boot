(set-env!  :dependencies '[[adzerk/boot-cljs          "1.7.48-SNAPSHOT"]
                           [adzerk/boot-reload        "0.3.1"]
                           [org.clojure/clojurescript "1.7.48"]
                           [org.clojure/clojure       "1.7.0"]
                           [tailrecursion/boot-hoplon "0.1.4"]
                           [tailrecursion/hoplon      "6.0.0-alpha7"]]
          :source-paths #{"src"}
          :resource-paths #{"assets"})

(require
  '[adzerk.boot-cljs          :refer [cljs]]
  '[adzerk.boot-reload        :refer [reload]]
  '[tailrecursion.boot-hoplon :refer [hoplon prerender haml]])

(deftask after-checkout
  [s sym SYM sym   "The sym"
   a arg ARG [edn] "The arg"]
  (let [next (delay (require (symbol (namespace sym)))
                    ((apply @(resolve sym) arg) identity))]
    (with-pre-wrap [fileset]
      (@next fileset))))

(deftask dev
  "Build hoplon-minimal for local development."
  []
  (comp
    (watch)
    (speak)
    #_(after-checkout :sym 'tailrecursion.boot-hoplon/haml)
    #_(after-checkout :sym 'tailrecursion.boot-hoplon/hoplon)
    (haml)
    (hoplon)
    (reload)
    (cljs)))

(deftask prod
  "Build hoplon-minimal for production deployment."
  []
  (comp
    #_(after-checkout :sym 'tailrecursion.boot-hoplon/haml)
    #_(after-checkout :sym 'tailrecursion.boot-hoplon/hoplon)
    (haml)
    (hoplon)
    (cljs :optimizations :advanced)
    (prerender)
    #_(after-checkout :sym 'tailrecursion.boot-hoplon/prerender)))
