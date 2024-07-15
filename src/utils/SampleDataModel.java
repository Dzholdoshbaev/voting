package utils;

import objects.Candidate;

import java.util.List;

public class SampleDataModel {
    private final List<Candidate> candidates;

    {
        candidates = JsonUtils.readCandidates();
    }

    public SampleDataModel() {
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void saveCandidates(List<Candidate> candidates){
        JsonUtils.saveCandidates(candidates);
    }

    public Candidate findCandidate(String candidateId) {
        return candidates.stream()
                .filter(candidate -> candidate.getCandidate_id().equals(candidateId))
                .findFirst()
                .orElse(null);
    }

    public void findCandidateAndAddVote(String candidateId) {
        candidates.stream()
                .filter(candidate -> candidate.getCandidate_id().equals(candidateId))
                .forEach(candidate -> candidate.setTotalVotes(candidate.getTotalVotes() + 1));
        int allCandidatesVotes = candidates.stream()
                .mapToInt(Candidate::getTotalVotes)
                .sum();
        candidates.stream()
                .forEach(candidate -> candidate.setPercent(Common.calculatePercentage(candidate.getTotalVotes(), allCandidatesVotes)));

        saveCandidates(candidates);
    }

}
