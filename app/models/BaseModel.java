package models;

import io.ebean.Model;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

/**
 * Superclass defining common database relevant fields
 */
@MappedSuperclass
public class BaseModel extends Model {
   @Id
   private Long id;

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }
}
