package com.example.Lankatools.dto;

public class AdminStatsDto {

    private long totalUsers;
    private long totalTools;
    private long totalBookings;
    private long pendingApprovals;

    public AdminStatsDto(long totalUsers, long totalTools,
                         long totalBookings, long pendingApprovals) {
        this.totalUsers = totalUsers;
        this.totalTools = totalTools;
        this.totalBookings = totalBookings;
        this.pendingApprovals = pendingApprovals;
    }

    public long getTotalUsers() { return totalUsers; }
    public long getTotalTools() { return totalTools; }
    public long getTotalBookings() { return totalBookings; }
    public long getPendingApprovals() { return pendingApprovals; }

}
