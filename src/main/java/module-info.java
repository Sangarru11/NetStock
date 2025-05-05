module com.github.Sangarru11 {
    requires java.naming;
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.desktop;
    requires jdk.compiler;

    opens com.github.Sangarru11 to javafx.fxml;
    opens com.github.Sangarru11.Model.Entity to org.hibernate.orm.core;
    exports com.github.Sangarru11;
    exports com.github.Sangarru11.View;
}
