class UrlMappings {
    static mappings = {
        "/user/$id?"(controller: "user", parseRequest: true){
            action = [GET: "show", PUT: "update", DELETE: "delete", POST: "save"]
        }
       "/admin/$adminController/$action?/$id?"(controller: "admin")

         "/$controller/$action?/$id?"{
          constraints {
             // apply constraints here
          }
        }
        "/"(controller:"index")
    }

}
