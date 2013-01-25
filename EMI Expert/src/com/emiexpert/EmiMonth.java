package com.emiexpert;

public class EmiMonth {
	
	private double mOpeningBal;
	private double mInterest;
	private double mPrinciple;
	private double mEmi;
	
	public EmiMonth(double openingBal,double emi,double interest) {
		// TODO Auto-generated constructor stub
		this.mOpeningBal = openingBal;
		this.mEmi = emi;
		calInterest(interest);
		calPrinciple();
	}
	
	//getters
	public double getOpeningBal(){
		return mOpeningBal;
	}
	
	public double getInterest(){
		return mInterest;
	}
	
	public double getPrinciple(){
		return mPrinciple;
	}
	
	public double getClosingPrinciple(){
		return mOpeningBal - mPrinciple;
	}
	
	public void calInterest(double i){
		mInterest = mOpeningBal*i;
	}
	
	public void calPrinciple(){
		mPrinciple = mEmi - mInterest;
	}
	
}
