package com.example.fairshare;

public class ExpenseShare {

        public int expenseRefId;
        public int memberId;
        public double shareAmount;
        public int paidStatus;
        public String memberName;


    public ExpenseShare(int expenseRefId, int memberId, double shareAmount, int paidStatus, String memberName) {
        this.expenseRefId = expenseRefId;
        this.memberId = memberId;
        this.shareAmount = shareAmount;
        this.paidStatus = paidStatus;
        this.memberName = memberName;
    }

    public int getExpenseRefId() {
        return expenseRefId;
    }
    public String getmemberName() {
        return memberName;
    }

    public int getMemberId() {
        return memberId;
    }

    public double getShareAmount() {
        return shareAmount;
    }

    public int getPaidStatus() {
        return paidStatus;
    }
}
