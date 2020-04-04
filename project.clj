(defproject explo "lein-git-inject/version"

  :dependencies [[javax.xml.bind/jaxb-api "2.3.0"]
                 [org.clojure/clojure       "1.10.1"]
                 [org.clojure/clojurescript "1.10.597"
                  :exclusions [com.google.javascript/closure-compiler-unshaded
                               org.clojure/google-closure-library
                               org.clojure/google-closure-library-third-party]]
                 [thheller/shadow-cljs      "2.8.83"]
                 [reagent                   "0.10.0"]
                 [re-frame                  "RELEASE"]]

  :plugins      [[day8/lein-git-inject "0.0.11"]
                 [lein-figwheel "0.5.19"]
                 [lein-shadow          "0.1.7"]]

  

  :middleware   [leiningen.git-inject/middleware]

  :clean-targets ^{:protect false} [:target-path
                                    "shadow-cljs.edn"
                                    "package.json"
                                    "package-lock.json"
                                    "resources/public/js"]

  :cljsbuild {:builds [{:id "explo"
                        :source-paths ["src/"]
                        :figwheel true
                        :compiler {:main "explo.core"
                                   :asset-path "js/out" ;;???
                                   :output-to "resources/public/js/client.js"
                                   :output-dir "resources/public/js"}}]}

  :shadow-cljs {:nrepl  {:port 8777}

                :builds {:client {:target     :browser
                                  :output-dir "resources/public/js"
                                  :modules    {:client {:init-fn explo.core/run}}
                                  :devtools   {:http-root "resources/public"
                                               :http-port 8280}}}}

  :aliases {"dev-auto" ["shadow" "watch" "client"]})