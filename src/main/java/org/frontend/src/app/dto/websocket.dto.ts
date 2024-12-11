export interface SummaryPool {
  threadPoolSize: number;
  ticketPoolSize: number;
  purchasePoolSize: number
}

export interface SummarySimulation {
  customerCount: number;
  vendorCount: number;
  activeCustomerCount: number;
  activeVendorCount: number;
}
