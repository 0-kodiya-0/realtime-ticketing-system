package org.backend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.backend.annotation.IsObjectIdValid;
import org.backend.dto.userdetails.Person;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Admin extends Person {
    private ObjectId _id;

    public void set_id(@IsObjectIdValid ObjectId _id) {
        this._id = _id;
    }
}
