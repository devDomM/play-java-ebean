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

    @Inject
    public ComputerRepository() {}

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

    /**
     * find a computer by id
     * @param id id to lookup
     * @return optional containing the computer object
     */
    public Optional<Computer> lookup(Long id) {
        return DB.find(Computer.class).setId(id).findOneOrEmpty();
    }

    /**
     * update a computer entity
     * @param id id of the database entity to update
     * @param newComputerData data to update
     */
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

    /**
     * delete a computer entity from the database
     * @param id id of the computer to delete
     */
    public void delete(Long id) {
        DB.delete(Computer.class, id);
    }

    /**
     * create a new computer database entry
     * @param computer object to persist
     */
    public void insert(Computer computer) {
         computer.setId(System.currentTimeMillis()); // not ideal, but it works
         DB.insert(computer);
    }
}
