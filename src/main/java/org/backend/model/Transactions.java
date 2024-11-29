package org.backend.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Transactions {
    private ObjectId _id;
    private List<Ticket> ticket;
    private Customer buyer;
}
