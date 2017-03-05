(ns myapp.views
  (:require [hiccup.core :refer (html)]
            [myapp.posts :as posts]
            [hiccup.form :as f]))

(defn layout [title & content]
  (html
    [:head [:title title]
     [:style "body {padding-top: 5%;
                    margin-left: 20%;
                    margin-right: 50px;
                    background-color: #ccd5e5;}
                    h1 { color: #ff4411; font-size: 48px; font-family: 'Signika', sans-serif; padding-bottom: 4px; }
                    h2 { color: #858757; font-size: 36px; font-family: 'Signika', sans-serif; padding-bottom: 2px; }
                    h3 { color: #858742; font-size: 18px; font-family: 'Signika', sans-serif; padding-bottom: 2px; padding-top: 15px; }
                    h4 { color: #858712; font-size: 14px; font-family: 'Signika', sans-serif; padding-bottom: 1px; }
                    p { font-family: 'Inder', sans-serif; line-height: 6px; color: #666;}
                    section { background-color: white; padding-left: 15px; padding-right: 0.0002px; padding-bottom: 2px;}
                    a {  text-decoration: none; margin-top: 15px; margin-bottom: 15px; margin-right: 15px; display: inline-block;  width: 75px; height: 15px;  background: #4E9CAF; padding: 10px; text-align: center; border-radius: 5px; color: white; font-weight: bold;}
                    label {color: #858712; margin-right: 15px; font-family: 'Signika', sans-serif; padding-bottom: 1px;}
                    input[type=text] {padding:5px; border:2px solid #ccc; -webkit-border-radius: 5px;   border-radius: 5px; width: 450px; margin-bottom: 15px;}
                    textarea {width: 495px;height: 120px; border: 3px solid #ccc; -webkit-border-radius: 5px;   border-radius: 5px; padding: 5px;font-family: Tahoma, sans-serif;background-position: bottom right; background-repeat: no-repeat; margin-bottom: 15px}
                    input[type=submit] {margin-top: 15px; margin-bottom: 15px; margin-right: 15px; display: inline-block;  width: 65px; height: 35px;  background: #4E9CAF; padding: 10px; text-align: center; border-radius: 5px; color: white; font-weight: bold;}
                    "]]
    [:body content]))

(defn main-page []
  (layout "News portal"
          [:h1 "News"]
          [:p "Welcome to news portal. This is a place where you can find a lot of interesting stuff about sports, media, culture etc. Welcome to news portal. This is a place where you can find a lot of interesting stuff about sports, media, culture etc. Welcome to news portal. This is a place where you can find a lot of interesting stuff about sports, media, culture etc. Welcome to news portal. This is a place where you can find a lot of interesting stuff about sports, media, culture etc."]))

(defn admin-post-summary [post]
      (let [id (:id post)
            title (:title post)
            short-body (:body post)
            created_at (:created_at post)]
           [:section
            [:h3 title]
            [:h4 created_at]
            [:section (str short-body " ...")]
            [:section.actions
             [:a {:href (str "/admin/" id "/more")} "Read more"]
             [:a {:href (str "/admin/" id "/edit")} "Edit"]
             [:a {:href (str "/admin/" id "/delete")} "Delete"]]]))

(defn post-summary [post]
  (let [id (:id post)
        title (:title post)
        short-body (:short-body post)
        created_at (:created_at post)]
    [:section
     [:h3 title]
     [:h4 created_at]
     [:section (str short-body "...")]
     [:section.actions
      [:a {:href (str "/posts/" id "/more")} "Read more"]]]))

(defn admin-blog-page []
      (layout "News portal - Administer page"
              [:h1 "Administer page"]
              [:h2 "All my news"]
              [:a {:href "/admin/add"} "Add"]
              (map #(admin-post-summary %) (posts/all))))

(defn blog-page []
  (layout "News portal"
          [:h1 "News"]
          [:h2 "All news"]
          (map #(post-summary %) (posts/all))))

(defn add-post []
      (layout "News portal - Add Post"
              (list
                [:h2 "Add Post"]
                (f/form-to [:post "/admin/create"]
                           (f/label "title" "Title")
                           (f/text-field "title") [:br]
                           (f/label "short-body" "Short body") [:br]
                           (f/text-area {:rows 10} "short-body") [:br]
                           (f/label "body" "Body") [:br]
                           (f/text-area {:rows 20} "body") [:br]
                           (f/submit-button "Save")))))
(defn edit-post [id]
      (layout "News portal - Edit Post"
              (list
                (let [post (posts/get id)]
                     [:h2 (str "Edit Post " id)]
                     (f/form-to [:post "save"]
                                (f/label "title" "Title")
                                (f/text-field "title" (:title post)) [:br]
                                (f/label "short-body" "Short body") [:br]
                                (f/text-area {:rows 10} "short-body" (:short-body post)) [:br]
                                (f/label "body" "Body") [:br]
                                (f/text-area {:rows 20} "body" (:body post)) [:br]
                                (f/submit-button "Save"))))))

(defn read-more-admin [id]
  (layout "News portal - Post"
          (list
            (let [post (posts/get id)]
              [:section
               [:h3 (:title post)]
               [:section (:body post)]
               [:p (str "Rating: " (- (:likes post) (:dislikes post)))]]
              ))))
(defn read-more [id]
  (layout "News portal - Post"
          (list
            (let [post (posts/get id)]
              [:section
               [:h3 (:title post)]
               [:section (:body post)]
               [:p (str "Number of views: " (:num_of_views post))]
               [:p (str "Rating: " (- (:likes post) (:dislikes post)))]
               [:a {:href (str "/like/" id)} "Like"]
               [:a {:href (str "/dislike/" id)} "Dislike"]]
              ))))