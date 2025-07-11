export enum CustomerType {
  NOT_VIP = 'NOT_VIP', VIP = 'VIP'
}

export enum PurchaseStatus{
  PURCHASED = "PURCHASED", PENDING="PENDING"
}

export interface CustomerDto {
  id: string;
  type: CustomerType;
}

export interface VendorDto {
  id: string;
}

export interface TicketDto {
  id: string;
  vendorId: string;
  quantity: number;
  boughtQuantity: number;
}

export interface PurchaseDto {
  id: string;
  customerId: string;
  ticketId: string;
  purchaseDate: Date;
  purchaseStatus: PurchaseStatus;
}

