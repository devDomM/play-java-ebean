package controllers;

import io.ebean.PagedList;
import models.Computer;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import repository.CompanyRepository;
import repository.ComputerRepository;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import java.util.Map;
import java.util.Optional;

/**
 * Manage a database of computers
 */
public class HomeController extends Controller {

    private final ComputerRepository computerRepository;
    private final CompanyRepository companyRepository;
    private final FormFactory formFactory;
    private final MessagesApi messagesApi;

    @Inject
    public HomeController(FormFactory formFactory,
                          ComputerRepository computerRepository,
                          CompanyRepository companyRepository,
                          HttpExecutionContext httpExecutionContext,
                          MessagesApi messagesApi) {
        this.computerRepository = computerRepository;
        this.formFactory = formFactory;
        this.companyRepository = companyRepository;
        this.messagesApi = messagesApi;
    }

    /**
     * This result directly redirect to application home.
     */
    private final Result GO_HOME = Results.redirect(
        routes.HomeController.list(0, "name", "asc", "")
    );

    /**
     * Handle default path requests, redirect to computers list
     */
    public Result index() {
        return GO_HOME;
    }

    /**
     * Display the paginated list of computers.
     *
     * @param page   Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order  Sort order (either asc or desc)
     * @param filter Filter applied on computer names
     */
    public Result list(Http.Request request, int page, String sortBy, String order, String filter) {
        // Run a db operation in another thread (using DatabaseExecutionContext)
        PagedList<Computer> pagedComputerList = computerRepository.page(page, 10, sortBy, order, filter);

        return ok(views.html.list.render(pagedComputerList, sortBy, order, filter, request, messagesApi.preferred(request)));
    }

    /**
     * Display the 'edit form' of a existing Computer.
     *
     * @param id Id of the computer to edit
     */
    public Result edit(Http.Request request,Long id) {

        // Run a db operation in another thread (using DatabaseExecutionContext)
        Map<String, String> companyOptions = companyRepository.options();

        Optional<Computer> computer = computerRepository.lookup(id);

        Form<Computer> computerForm = formFactory.form(Computer.class);
        if (computer.isPresent()) {
            computerForm = computerForm.fill(computer.get());
        }

        return ok(views.html.editForm.render(id, computerForm, companyOptions, request, messagesApi.preferred(request)));
    }

    /**
     * Handle the 'edit form' submission
     *
     * @param id Id of the computer to edit
     */
    public Result update(Http.Request request, Long id) throws PersistenceException {
        Form<Computer> computerForm = formFactory.form(Computer.class).bindFromRequest(request);
        if (computerForm.hasErrors()) {
            // Run companies db operation and then render the failure case
            Map<String, String> companyOptions = companyRepository.options();
            return badRequest(views.html.editForm.render(id, computerForm, companyOptions, request, messagesApi.preferred(request)));
        } else {
            Computer newComputerData = computerForm.get();
            // Run update operation and then flash and then redirect
            computerRepository.update(id, newComputerData);

            return GO_HOME.flashing("success", "Computer " + newComputerData.getName() + " has been updated");
        }
    }

    /**
     * Display the 'new computer form'.
     */
    public Result create(Http.Request request) {
        Form<Computer> computerForm = formFactory.form(Computer.class);
        // Run companies db operation and then render the form
        Map<String, String> companyOptions = companyRepository.options();
        return ok(views.html.createForm.render(computerForm, companyOptions, request, messagesApi.preferred(request)));
    }

    /**
     * Handle the 'new computer form' submission
     */
    public Result save(Http.Request request) {
        Form<Computer> computerForm = formFactory.form(Computer.class).bindFromRequest(request);
        if (computerForm.hasErrors()) {
            // Run companies db operation and then render the form
            Map<String, String> companyOptions = companyRepository.options();
            return badRequest(views.html.createForm.render(computerForm, companyOptions, request, messagesApi.preferred(request)));
        }

        Computer computer = computerForm.get();
        // Run insert db operation, then redirect
        computerRepository.insert(computer);

        return GO_HOME.flashing("success", "Computer " + computer.getName() + " has been created");
    }

    /**
     * Handle computer deletion
     */
    public Result delete(Long id) {
        // Run delete db operation, then redirect
        computerRepository.delete(id);
        return GO_HOME.flashing("success", "Computer has been deleted");
    }

}
            
