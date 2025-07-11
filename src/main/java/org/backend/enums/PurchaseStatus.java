package org.backend.enums;

/**
 * Enum representing the status of a purchase that create by a customer
 *
 * PURCHASED: Represent the purchase that have been queued and paid for it by a customer
 * PENDING: Represent the purchase that have been queued but not paid, by a customer
 */
public enum PurchaseStatus {
    PURCHASED, PENDING;
}
