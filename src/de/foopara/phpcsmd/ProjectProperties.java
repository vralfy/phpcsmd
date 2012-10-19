
package de.foopara.phpcsmd;

import de.foopara.phpcsmd.ui.PropertyPanel;
import java.util.ResourceBundle;
import javax.swing.JComponent;
import org.netbeans.spi.project.ui.support.ProjectCustomizer;
import org.netbeans.spi.project.ui.support.ProjectCustomizer.Category;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

/**
 *
 * @author n.specht
 */
public class ProjectProperties implements ProjectCustomizer.CompositeCategoryProvider {

    @ProjectCustomizer.CompositeCategoryProvider.Registration(projectType = "org-netbeans-modules-php-project", position = 900)
    public static ProjectProperties createProperties() {
        return new ProjectProperties();
    }

    @Override
    public Category createCategory(Lookup context) {
        ResourceBundle bundle = NbBundle.getBundle(ProjectProperties.class);
        for (String s : bundle.keySet()) {
            System.err.println(s);
        }
        return  ProjectCustomizer.Category.create("phpcsmd", "Phpcsmd", null);
    }

    @Override
    public JComponent createComponent(Category category, Lookup context) {
        return new PropertyPanel();
    }

}
