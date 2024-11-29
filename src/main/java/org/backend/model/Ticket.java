package org.backend.model;

import org.backend.annotation.ValueOfEnum;
import org.backend.enums.TicketCategory;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Ticket {
    private ObjectId _id;
    private Vendor vendor;
    private String title;
    private String description;
    private int price;
    private int maxTicketCount;
    private int boughtTicketCount;
    @ValueOfEnum(enumClass = TicketCategory.class)
    private String ticketCategory;
}
