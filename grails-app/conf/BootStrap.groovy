import com.redhat.theses.Organization
import com.redhat.theses.auth.User

class BootStrap {

    def init = { servletContext ->
        if(!User.count() ){
            def o = new Organization(name: 'Masaryk University')
                    .addToMembers(new User(username: 'jcechace', password: "passw0rd"))
                    .save()
        }
    }
    def destroy = {
    }
}
