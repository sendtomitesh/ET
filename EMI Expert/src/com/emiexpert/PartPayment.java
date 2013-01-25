package com.emiexpert;


public class PartPayment {
	
	private int mWhichMonth;
	private double mAmount;
	private PartPaymentType mPaymentType;
	
	public static enum PartPaymentType{
		
		LESS_DURATION,
		LESS_EMI
	}
	
	public PartPayment(int month,double amount,PartPaymentType type) {
		// TODO Auto-generated constructor stub
		this.mWhichMonth = month;
		this.mAmount = amount;
		this.mPaymentType = type;
	}
	
	//getter 
	public int getMonth(){
		return this.mWhichMonth;
	}
	
	public double getAmount(){
		return this.mAmount;
	}
	
	public PartPaymentType getPaymentType(){
		return this.mPaymentType;
	}
	
}
