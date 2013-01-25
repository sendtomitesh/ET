package com.emiexpert;

import java.util.ArrayList;

import com.emiexpert.PartPayment.PartPaymentType;

public class Loan {
	private double mLoanInterest;
	private double mLoanPrinciple;
	private int mLoanDuration;
	private ArrayList<EmiMonth> mEmiMonths;
	private ArrayList<PartPayment> mPartPayments = null;

	public Loan(double principle, double interest, int duration) {
		this.mLoanPrinciple = principle;
		this.mLoanInterest = interest;
		this.mLoanDuration = duration;
		mEmiMonths = new ArrayList<EmiMonth>();
		generateEmis();
	}

	public double getEMI(int month) {

		double emi = 0.00;
		if (mPartPayments == null || mPartPayments.get(month).getPaymentType() == PartPayment.PartPaymentType.LESS_DURATION) {
			mLoanInterest = (mLoanInterest / 1200);
			emi = mLoanPrinciple * mLoanInterest
					* Math.pow(1 + mLoanInterest, mLoanDuration)
					/ (Math.pow(1 + mLoanInterest, mLoanDuration) - 1);
			
		} 
		else if (mPartPayments.get(month).getPaymentType() == PartPayment.PartPaymentType.LESS_EMI) {
			emi = 0.00;
		} 
		return emi;
	}

	public void initalizePartPayment() {
		mPartPayments = new ArrayList<PartPayment>();
	}

	public void addPartPayment(PartPayment partPayment) {
		mPartPayments.add(partPayment);
	}

	public void generateEmis() {
		EmiMonth emiMonth;
		double openingBal = mLoanPrinciple;

		for (int i = 1; i <= mLoanDuration; i++) {

			emiMonth = new EmiMonth(openingBal, getEMI(i), getI());
			openingBal = emiMonth.getClosingPrinciple();
			mEmiMonths.add(emiMonth);

		}
	}

	public double getI() {
		return 0.00;
	}

	public ArrayList<EmiMonth> getAllEmis() {
		return mEmiMonths;
	}

	public EmiMonth getEmi(int month) {
		if (month <= mLoanDuration) {
			return mEmiMonths.get(month);
		} else {
			return null;
		}
	}

}
