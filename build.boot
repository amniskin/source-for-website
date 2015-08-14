(set-env!  :dependencies '[[adzerk/boot-cljs          "1.7.48-SNAPSHOT"]
                           [adzerk/boot-reload        "0.3.1"]
                           [org.clojure/clojurescript "1.7.48"]
                           [org.clojure/clojure       "1.7.0"]
                           #_[tailrecursion/boot-hoplon "0.1.0"]
                           [tailrecursion/hoplon      "6.0.0-alpha2"]]
           :source-paths #{"src"}
		  		 :resource-paths #{"assets"})

(require
'[adzerk.boot-cljs          :refer [cljs]]
'[adzerk.boot-reload        :refer [reload]]
#_'[tailrecursion.boot-hoplon :refer [hoplon prerender haml]])

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
#_(watch)
(speak)
(after-checkout :sym 'tailrecursion.boot-hoplon/haml)
(after-checkout :sym 'tailrecursion.boot-hoplon/hoplon)
    (reload)
    (cljs)))

(deftask prod
  "Build hoplon-minimal for production deployment."
  []
  (comp
    (after-checkout :sym 'tailrecursion.boot-hoplon/haml)
    (after-checkout :sym 'tailrecursion.boot-hoplon/hoplon)
    (cljs :optimizations :advanced)
    (after-checkout :sym 'tailrecursion.boot-hoplon/prerender)))
