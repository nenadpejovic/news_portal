(ns myapp.posts
    (:refer-clojure :exclude [get])
    (:require [clojure.java.jdbc :as j]
      [clojure.java.jdbc.sql :as s]))

(def mysql-db {:subprotocol "mysql"
               :subname "//localhost:3306/myapp"
               :user "root"
               :password ""
               :zeroDateTimeBehavior "convertToNull"})

(defn all []
      (j/query mysql-db
               (s/select * :posts)))

(defn get_comments [id]
  (j/query mysql-db
           (s/select * :comments (s/where {:postId id}))))

(defn get [id]
      (first (j/query mysql-db
                      (s/select * :posts (s/where {:id id})))))
(def now
  (str (java.sql.Timestamp. (System/currentTimeMillis))))

(defn retrieve_num_of_views [id]
  (j/query mysql-db
           (s/select "num_of_views" :posts (s/where {:id id}))))

(defn update_num_of_views [num_of_views id]
  (j/update! mysql-db :posts {:num_of_views num_of_views} (s/where {:id id})))

(defn increase_num_of_views [id]
  (let [num_of_views (:num_of_views (nth (retrieve_num_of_views id) 0))]
    (update_num_of_views (+ num_of_views 1) id)))

(defn decrease_num_of_views [id]
  (let [num_of_views (:num_of_views (nth (retrieve_num_of_views id) 0))]
    (update_num_of_views (- num_of_views 1) id))
  )

(defn like_post [id]
  (let [likes (:likes (nth (j/query mysql-db (s/select "likes" :posts (s/where {:id id}))) 0))]
    (j/update! mysql-db :posts {:likes (+ likes 1)} (s/where {:id id}))
    (decrease_num_of_views id))
  )

(defn dislike_post [id]
  (let [dislikes (:dislikes (nth (j/query mysql-db (s/select "dislikes" :posts (s/where {:id id}))) 0))]
    (j/update! mysql-db :posts {:dislikes (+ dislikes 1)} (s/where {:id id}))
    (decrease_num_of_views id))
  )

(defn add_comment [id params]
  (j/insert! mysql-db :comments (merge params {:postId id})))

(defn create [params]
      (j/insert! mysql-db :posts (merge params {:created_at now :updated_at now})))

(defn save [id params]
      (j/update! mysql-db :posts params (s/where {:id id})))

(defn delete [id]
      (j/delete! mysql-db :posts (s/where {:id id})))