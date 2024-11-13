package repository;

import io.ebean.DB;
import io.ebean.PagedList;
import io.ebean.Transaction;
import models.Computer;

import javax.inject.Inject;
import java.util.Optional;
/**
 * A repository that executes database operations in a different
 * execution context.
 */
public class ComputerRepository {

    private final DatabaseExecutionContext executionContext;

    @Inject
    public ComputerRepository(DatabaseExecutionContext executionContext) {
        this.executionContext = executionContext;
    }

    /**
     * Return a paged list of computer
     *
     * @param page     Page to display
     * @param pageSize Number of computers per page
     * @param sortBy   Computer property used for sorting
     * @param order    Sort order (either or asc or desc)
     * @param filter   Filter applied on the name column
     */
    public PagedList<Computer> page(int page, int pageSize, String sortBy, String order, String filter) {
        return DB.find(Computer.class)
                    .fetch("company").where()
                    .ilike("name", "%" + filter + "%")
                    .orderBy(sortBy + " " + order)
                    .setFirstRow(page * pageSize)
                    .setMaxRows(pageSize)
                    .findPagedList();
    }

    public Optional<Computer> lookup(Long id) {
        return DB.find(Computer.class).setId(id).findOneOrEmpty();
    }

    public void update(Long id, Computer newComputerData) {
        try(Transaction txn = DB.beginTransaction()) {
            Computer savedComputer = DB.find(Computer.class).setId(id).findOne();
            if (savedComputer != null) {
                savedComputer.fill(newComputerData);
                DB.update(savedComputer);
                txn.commit();
            }
        }
    }

    public void delete(Long id) {
        DB.delete(Computer.class, id);
    }

    public void insert(Computer computer) {
         computer.setId(System.currentTimeMillis()); // not ideal, but it works
         DB.insert(computer);
    }
}
