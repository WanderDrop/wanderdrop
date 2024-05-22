package com.wanderdrop.wserver.startup;

import com.wanderdrop.wserver.model.DeletionReason;
import com.wanderdrop.wserver.repository.DeletionReasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements ApplicationRunner {

    private DeletionReasonRepository deletionReasonRepository;

    @Autowired
    public DataLoader(DeletionReasonRepository deletionReasonRepository) {
        this.deletionReasonRepository = deletionReasonRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (deletionReasonRepository.count() == 0) {
            List<String> reasons = Arrays.asList(
                    "Violation of Community Guidelines",
                    "Inappropriate Content",
                    "Irrelevant or Off-topic",
                    "Duplicate Content",
                    "Trolling or Disruptive Behaviour",
                    "Commercial or Promotional Content",
                    "Political or Religious Sensitivity"
            );
            for (String reason : reasons) {
                DeletionReason deletionReason = new DeletionReason();
                deletionReason.setReasonMessage(reason);
                deletionReason.setDefaultReason(true);
                deletionReasonRepository.save(deletionReason);
            }
        }
    }
}
