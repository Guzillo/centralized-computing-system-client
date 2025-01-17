import Enums.StatisticType;

import java.util.HashMap;
import java.util.Map;

public class Statistician extends Thread{
    Map<Integer, OperationData> operationsMap;
    Map<Integer, OperationData> tmpOperationsMap;
    Integer totalOperationsCount = 0;
    Integer totalErrorsCount = 0;
    int totalClientCount = 0;
    Integer mapCurrentIndex = 0;
    Integer totalResultsSum = 0;

    Integer tmpErrorsCount = 0;
    Integer tmpOperationsCount = 0;
    Integer tmpClientsCount = 0;
    Integer tmpResultsSum = 0;
    boolean isFirstCycle = true;
    @Override
    public void run() {
        operationsMap = new HashMap<>();
        while (true) {
            tmpOperationsMap = new HashMap<>();

            try {
                if (!isFirstCycle) {
                    System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                    displayStatistics(StatisticType.Temporary);
                    mergeMapsAndCounters();
                    displayStatistics(StatisticType.Total);
                    System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&\n\n\n");
                }
                Thread.sleep(10_000);
                isFirstCycle = false;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public synchronized void addOperation(String operator, int arg1, int arg2, int result) {
        OperationData operationData = new OperationData(operator, arg1, arg2, result);
        tmpOperationsMap.put(totalOperationsCount, operationData);
        tmpOperationsCount++;
        tmpResultsSum += result;
    }

    public synchronized void mergeMapsAndCounters() {
        operationsMap.putAll(tmpOperationsMap);
        totalOperationsCount += tmpOperationsCount;
        totalClientCount += tmpClientsCount;
        totalErrorsCount += tmpErrorsCount;
        totalResultsSum += tmpResultsSum;

        tmpOperationsCount = 0;
        tmpClientsCount = 0;
        tmpErrorsCount = 0;
        tmpResultsSum = 0;
    }

    public synchronized void incrementErrorsCount() {
        tmpErrorsCount++;
    }

    public synchronized void incrementClientsCount() {
        tmpClientsCount++;
    }

    public void displayStatistics(StatisticType statisticType) {
        int clientsCount = 0;
        int resultsSum = 0;
        int errorsCount = 0;
        int operationsCount = 0;

        String chosenWelcome = "";
        String tmpStatsWelcome = "Statistics from the last 10 seconds: ";
        String totalStatsWelcome ="Total statistics: ";
        if (statisticType == StatisticType.Temporary) {
            clientsCount = tmpClientsCount;
            resultsSum = tmpResultsSum;
            errorsCount = tmpErrorsCount;
            chosenWelcome = tmpStatsWelcome;
            operationsCount = tmpOperationsCount;
        } else if (statisticType == StatisticType.Total) {
            clientsCount = totalClientCount;
            resultsSum = totalResultsSum;
            errorsCount = totalErrorsCount;
            chosenWelcome = totalStatsWelcome;
            operationsCount = totalOperationsCount;
        }

        System.out.println("-------------------------\n" +
                chosenWelcome + "\n\n" +
                "Number of clients: " + clientsCount + "\n" +
                "Summary of equations: " +  resultsSum + "\n" +
                "Number of equations: " + operationsCount + "\n" +
                "Number of wrong operations: " + errorsCount + "\n" +
                "-------------------------\n"
        );

    }

}