(ns myapp.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [myapp.views :as views]
            [ring.util.response :as resp]
            [myapp.posts :as posts]
            [ring.middleware.basic-authentication :refer :all]))

(defroutes public-routes
           (GET "/" [] (views/main-page))
           (GET "/posts/:id/more" [id] (do (posts/increase_num_of_views id)
                                           (views/read-more id)))
           (GET "/posts" [] (views/blog-page))
           (GET "/like/:id" [id] (do (posts/like_post id)
                                     (resp/redirect (str "/posts/" id "/more"))))
           (GET "/dislike/:id" [id] (do (posts/dislike_post id)
                                     (resp/redirect (str "/posts/" id "/more"))))
           (POST "/comment/add/:id" [id & params] (do (posts/add_comment id params)
                                            (resp/redirect (str "/posts/" id "/more"))))
           (route/resources "/"))

(defroutes protected-routes
           (GET "/admin" [] (views/admin-blog-page))
           (GET "/admin/add" [] (views/add-post))
           (POST "/admin/create" [& params]
                 (do (posts/create params)
                     (resp/redirect "/admin")))
           (GET "/admin/:id/edit" [id] (views/edit-post id))
           (POST "/admin/:id/save" [& params]
                 (do (posts/save (:id params) params)
                     (resp/redirect "/admin")))
           (GET "/admin/:id/more" [id] (views/read-more-admin id) )
           (GET "/admin/:id/delete" [id]
                (do (posts/delete id)
                    (resp/redirect "/admin"))))

(defn authenticated? [name pass]
  (and (= name "user")
       (= pass "pass")))

(defroutes app-routes
           public-routes
           (wrap-basic-authentication protected-routes authenticated?)
           (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
