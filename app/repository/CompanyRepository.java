package repository;

import io.ebean.DB;
import models.Company;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class CompanyRepository {

    private final DatabaseExecutionContext executionContext;

    @Inject
    public CompanyRepository(DatabaseExecutionContext executionContext) {
        this.executionContext = executionContext;
    }

    public Map<String, String> options() {
        List<Company> companyList = DB.find(Company.class).orderBy("name").findList();

        HashMap<String, String> options = new LinkedHashMap<>();
        for (Company c : companyList) {
            options.put(c.getId().toString(), c.getName());
        }
        return options;
    }

}
