/*
Created on Tue Jan 29 12:47:38 2019

@author: DoctorStrange

Course recommender system using JESS: rule-based system
*/

(reset)

;;(watch all)

(defglobal ?*course_list* = nil)
(defglobal ?*cur_user_profile* = nil)
(defglobal ?*cur_course_recommender* = nil)

;;Templates to hold the user profile and the course details;;

(deftemplate user_profile
    (declare
        (slot-specific TRUE)
    )
    (slot Name)
    (slot Age)
    (slot CurrentDomain)
    (slot ExperienceInYears)
    (slot ChangeInDomain)
    (multislot Skills)
    (slot MultipleSkills)
    (slot AvailableDurationInMonths)
)

(deftemplate domain_details
    (declare
        (slot-specific TRUE)
    )
    (slot DomainName)
    (multislot Courses)
)

(deftemplate course_details
    (declare
        (slot-specific TRUE)
    )
    (slot CourseName)
    (slot CourseDurationInMonths)
    (slot CourseDomain)
)

(deftemplate courses
    (declare
        (slot-specific TRUE)
    )
    (multislot CourseList)
)

(deftemplate course_recommender
    (declare
        (slot-specific TRUE)
    )
    (slot RecommendedDomain)
    (multislot RecommendedCourses)
    (slot Duration)
    (slot Decision)
)

;;Facts definition;;

;Current user profile;
(bind ?*cur_user_profile*
    (assert (user_profile))
)

;Course recommender;
(bind ?*cur_course_recommender*
    (assert (course_recommender))
)

;Math domain;
(bind ?math_course_1
    (assert 
        (course_details
            (CourseName Logic)
            (CourseDurationInMonths 2)
            (CourseDomain Math)
        )
    )
)

(bind ?math_course_2
    (assert 
        (course_details
            (CourseName Statistics)
            (CourseDurationInMonths 6)
            (CourseDomain Math)
        )
    )
)

(bind ?course_math
    (assert
        (domain_details
            (DomainName Math)
            (Courses ?math_course_1 ?math_course_2)
        )
    )
)

;Data Science domain;
(bind ?ds_course_1
    (assert 
        (course_details
            (CourseName Data_Analysis)
            (CourseDurationInMonths 4)
            (CourseDomain Data_Science)
        )
    )
)

(bind ?ds_course_2
    (assert 
        (course_details
            (CourseName Machine_Learning)
            (CourseDurationInMonths 6)
            (CourseDomain Data_Science)
        )
    )
)

(bind ?ds_course_3
    (assert 
        (course_details
            (CourseName Probability_And_Statistics)
            (CourseDurationInMonths 3)
            (CourseDomain Data_Science)
        )
    )
)

(bind ?course_ds
    (assert
        (domain_details
            (DomainName Data_Science)
            (Courses ?ds_course_1 ?ds_course_2 ?ds_course_3)
        )
    )
)

;Computer Science domain;
(bind ?cs_course_1
    (assert 
        (course_details
            (CourseName Software_development)
            (CourseDurationInMonths 12)
            (CourseDomain Computer_Science)
        )
    )
)

(bind ?cs_course_2
    (assert 
        (course_details
            (CourseName Mobile_development)
            (CourseDurationInMonths 8)
            (CourseDomain Computer_Science)
        )
    )
)

(bind ?cs_course_3
    (assert 
        (course_details
            (CourseName Web_development)
            (CourseDurationInMonths 10)
            (CourseDomain Computer_Science)
        )
    )
)

(bind ?cs_course_4
    (assert 
        (course_details
            (CourseName Algorithms)
            (CourseDurationInMonths 4)
            (CourseDomain Computer_Science)
        )
    )
)

(bind ?cs_course_5
    (assert 
        (course_details
            (CourseName Design)
            (CourseDurationInMonths 6)
            (CourseDomain Computer_Science)
        )
    )
)

(bind ?course_cs
    (assert
        (domain_details
            (DomainName Computer_Science)
            (Courses ?cs_course_1 ?cs_course_2 ?cs_course_3 ?cs_course_4 ?cs_course_5)
        )
    )
)

;;Global list of courses

(bind ?*course_list*
    (assert
        (courses
            (CourseList ?course_math ?course_ds ?course_cs)
        )
    )
)

;;Function definitions;;

;Input functions;

(deffunction fetchUserProfile()
    (displayBorder)
    (printout t "Welcome to the course recommender system!" crlf)
    (displayBorder)
    (printout t "Please fill the user profile section for the system to work on the recommendations." crlf)
    (fetchUserName)
    ;;(fetchUserAge)
    (fetchUserCurrentDomain)
    ;;(fetchUserExperience)
    (fetchUserChangeInDomain)
    (fetchUserSkills)
    (fetchUserAvailableDuration)
)

(deffunction fetchUserName()
    (printout t crlf "Enter your name: ")
    (modify ?*cur_user_profile* (Name (read)))
)

(deffunction fetchUserAge()
    (printout t "Enter your age: ")
    (modify ?*cur_user_profile* (Age (read)))
)

(deffunction fetchUserCurrentDomain()
    (printout t crlf "The following domains are available currently." crlf)
    (bind ?indx 1)
    (foreach ?domain ?*course_list*.CourseList
        (printout t ?indx ".")
        (printout t ?domain.DomainName crlf)
        (bind ?indx (+ ?indx 1))
    )
    (printout t "Enter your domain : ")
    (bind ?user_choice (read))
    (bind ?indx 1)
    (bind ?valid_choice False)
    (foreach ?domain ?*course_list*.CourseList
        (if (eq ?indx ?user_choice) then
            (modify ?*cur_user_profile* (CurrentDomain ?domain.DomainName))
            (bind ?valid_choice True)
        )
        (bind ?indx (+ ?indx 1))
    )
    (if (eq ?valid_choice False) then
        (printout t "Please enter a valid choice!!!" crlf)
        (fetchUserCurrentDomain)
    )
)

(deffunction fetchUserExperience()
    (printout t "Enter your experience (in years) for your selected domain: ")
    (modify ?*cur_user_profile* (ExperienceInYears (read)))
)

(deffunction fetchUserChangeInDomain()
    (printout t crlf "Are you looking for a change in domain (Y/N): ")
    (bind ?user_choice (read))
    (if  (not (or (eq ?user_choice Y) (eq ?user_choice y) (eq ?user_choice N) (eq ?user_choice n))) then
        (printout t "Please enter 'Y' or 'N'!" crlf)
        (fetchUserChangeInDomain)
    else
        (modify ?*cur_user_profile* (ChangeInDomain ?user_choice))
    )
)

(deffunction fetchUserSkills()
    (printout t crlf "The following skills are available currently in your selected domain." crlf)
    (bind ?indx 1)
    (bind ?cur_domain nil)
    (foreach ?domain ?*course_list*.CourseList
        (if (eq ?domain.DomainName ?*cur_user_profile*.CurrentDomain) then
            (bind ?cur_domain ?domain)
            (foreach ?course ?domain.Courses
                (printout t ?indx ".")
                (printout t ?course.CourseName crlf)
                (bind ?indx (+ ?indx 1))
            )
            (break)
        )
    )
    (bind ?continue true)
    (bind ?user_choice ())
    (while (eq ?continue true)
        (printout t "Enter your skill : ")
        (bind ?user_ip (read))
        (if (member$ ?user_ip ?user_choice) then
            (printout t "Skill already selected!" crlf)
        elif (not (regexp \\d+ ?user_ip)) then
            (printout t "Please enter a valid choice!" crlf)
        elif (and (regexp \\d+ ?user_ip) (not (and (> ?user_ip 0) (<= ?user_ip (length$ ?cur_domain.Courses))))) then
            (printout t "Please enter a valid choice!" crlf)
        else
            (bind ?user_choice (insert$ ?user_choice (+ (length$ ?user_choice) 1) ?user_ip))
            (if (eq (length$ ?user_choice) (length$ ?cur_domain.Courses)) then
                (break)
            )
        )
        (bind ?yes_no_continue True)
        (while (eq ?yes_no_continue True)
            (printout t "Do you wish to enter another skill (Y/N)? ")
            (bind ?user_continue (read))
            (if  (or (eq ?user_continue Y) (eq ?user_continue y) (eq ?user_continue N) (eq ?user_continue n)) then
                (bind ?yes_no_continue False)
            )
        )
        (if (or (eq ?user_continue N) (eq ?user_continue n)) then
            (bind ?continue false)
        )
    )
    (bind ?indx 1)
    (bind ?skillCounter 0)
    (bind ?user_skills ())
    (foreach ?cur_domain_obj ?cur_domain.Courses
        (if (member$ ?indx ?user_choice) then
            (bind ?user_skills (insert$ ?user_skills (+ (length$ ?user_skills) 1) ?cur_domain_obj.CourseName))
            (bind ?skillCounter (+ ?skillCounter 1))
        )
        (bind ?indx (+ ?indx 1))
    )
    (modify ?*cur_user_profile* (Skills ?user_skills))
    (if (> ?skillCounter 1) then
        (modify ?*cur_user_profile* (MultipleSkills True))
    )
)

(deffunction fetchUserAvailableDuration()
    (printout t crlf "Enter the duration (in months) you are willing to commit for the courses: ")
    (bind ?user_ip (read))
    (while (not (regexp \\d+ ?user_ip)) then
        (printout t "Please enter a valid choice!" crlf)
        (printout t "Enter the duration (in months) you are willing to commit for the courses: ")
        (bind ?user_ip (read))
    )
    (while (< ?user_ip 0) then
        (printout t "Please enter a value greater than or equal to 0." crlf)
        (printout t "Enter the duration (in months) you are willing to commit for the courses: ")
        (bind ?user_ip (read))
    )
    (modify ?*cur_user_profile* (AvailableDurationInMonths ?user_ip))
)

;Rule functions;

(deffunction recommend_course_seq(?user ?recommender ?cur_domain)
    (foreach ?domain ?*course_list*.CourseList
        (if (eq ?domain.DomainName ?cur_domain) then
            (foreach ?course ?domain.Courses
                (if (and (not (member$ ?course.CourseName ?user.Skills)) (> (- ?recommender.Duration ?course.CourseDurationInMonths) 0)) then
                    (modify ?recommender (RecommendedCourses (insert$ ?recommender.RecommendedCourses (+ (length$ ?recommender.RecommendedCourses) 1) ?course.CourseName)))
                    (modify ?recommender (Duration (- ?recommender.Duration ?course.CourseDurationInMonths)))
                    (modify ?recommender (Decision Maybe))
                )
            )
            (break)
        )
    )
)

(deffunction assign_course(?user ?recommender ?course_sel)
    (foreach ?domain ?*course_list*.CourseList
        (if (eq ?domain.DomainName ?recommender.RecommendedDomain) then
            (foreach ?course ?domain.Courses
                (if (and (eq ?course.CourseName ?course_sel) (not (member$ ?course.CourseName ?recommender.RecommendedCourses)) (> (- ?recommender.Duration ?course.CourseDurationInMonths) 0)) then
                    (modify ?recommender (RecommendedCourses (insert$ ?recommender.RecommendedCourses (+ (length$ ?recommender.RecommendedCourses) 1) ?course.CourseName)))
                    (modify ?recommender (Duration (- ?recommender.Duration ?course.CourseDurationInMonths)))
                    (modify ?recommender (Decision Maybe))
                    (break)
                )
            )
            (break)
        )
    )
)

(deffunction check_assign_course(?user ?recommender ?course_sel $?course_dependencies)
    (bind ?toContinue True)
    (foreach ?dep_course ?course_dependencies
        (if (not (member$ ?dep_course ?user.Skills)) then
            (bind ?toContinue False)
            (break)
        )
    )
    (if (eq ?toContinue False) then
        (return)
    )
    (assign_course ?user ?recommender ?course_sel)
)

;Helper functions;
(deffunction displayBorder()
    (bind ?i 0)
    (while (< ?i 50)
        (printout t ":")
        (bind ?i (+ ?i 1))
    )
    (printout t crlf)
)

;Output function;

(deffunction displayRecommendations()
    (displayBorder)
    (if (eq ?*cur_course_recommender*.Decision nil) then
        (printout t "Sorry " ?*cur_user_profile*.Name ", we couldn't find any recommendations for you." crlf)
    else
        (printout t "Congratulations " ?*cur_user_profile*.Name ", we found the following courses in *" ?*cur_course_recommender*.RecommendedDomain "* domain to be suitable for your career growth." crlf)
        (bind ?i 1)
        (foreach ?cur_course ?*cur_course_recommender*.RecommendedCourses
            (printout t ?i ". " ?cur_course crlf)
            (bind ?i (+ ?i 1))
        )
    )
    (printout t crlf)
)

;;Rule definitions;;

(defrule math_change
    ?user <- (user_profile (CurrentDomain ?cd&: (eq ?cd Math)) (ChangeInDomain ?cid&: (or (eq ?cid Y) (eq ?cid y))))
    ?recommender <- (course_recommender (Duration ?d&: (eq ?d nil)))
    =>
    (modify ?recommender (Duration ?user.AvailableDurationInMonths))
    (modify ?recommender (RecommendedDomain Data_Science))
)

(defrule math_no_change
    ?user <- (user_profile (CurrentDomain ?cd&: (eq ?cd Math)) (ChangeInDomain ?cid&: (or (eq ?cid N) (eq ?cid n))))
    ?recommender <- (course_recommender (Duration ?d&: (eq ?d nil)))
    =>
    (modify ?recommender (Duration ?user.AvailableDurationInMonths))
    (modify ?recommender (RecommendedDomain Math))
)

(defrule ds_change
    ?user <- (user_profile (CurrentDomain ?cd&: (eq ?cd Data_Science)) (ChangeInDomain ?cid&: (or (eq ?cid Y) (eq ?cid y))))
    ?recommender <- (course_recommender (Duration ?d&: (eq ?d nil)))
    =>
    (modify ?recommender (Duration ?user.AvailableDurationInMonths))
    (modify ?recommender (RecommendedDomain Computer_Science))
)

(defrule ds_no_change
    ?user <- (user_profile (CurrentDomain ?cd&: (eq ?cd Data_Science)) (ChangeInDomain ?cid&: (or (eq ?cid N) (eq ?cid n))))
    ?recommender <- (course_recommender (Duration ?d&: (eq ?d nil)))
    =>
    (modify ?recommender (Duration ?user.AvailableDurationInMonths))
    (modify ?recommender (RecommendedDomain Data_Science))
)

(defrule cs_change
    ?user <- (user_profile (CurrentDomain ?cd&: (eq ?cd Computer_Science)) (ChangeInDomain ?cid&: (or (eq ?cid Y) (eq ?cid y))))
    ?recommender <- (course_recommender (Duration ?d&: (eq ?d nil)))
    =>
    (modify ?recommender (Duration ?user.AvailableDurationInMonths))
    (modify ?recommender (RecommendedDomain Data_Science))
)

(defrule cs_no_change
    ?user <- (user_profile (CurrentDomain ?cd&: (eq ?cd Computer_Science)) (ChangeInDomain ?cid&: (or (eq ?cid N) (eq ?cid n))))
    ?recommender <- (course_recommender (Duration ?d&: (eq ?d nil)))
    =>
    (modify ?recommender (Duration ?user.AvailableDurationInMonths))
    (modify ?recommender (RecommendedDomain Computer_Science))
)

(defrule math_no_change_action
    ?user <- (user_profile (CurrentDomain ?cd&: (eq ?cd Math)) (ChangeInDomain ?cid&: (or (eq ?cid N) (eq ?cid n))))
    ?recommender <- (course_recommender (Duration ?d&: (not (eq ?d nil))) (Decision ?dec&: (eq ?dec nil)))
    =>
    (recommend_course_seq ?user ?recommender ?user.CurrentDomain)
)

(defrule ds_no_change_action
    ?user <- (user_profile (CurrentDomain ?cd&: (eq ?cd Data_Science)) (ChangeInDomain ?cid&: (or (eq ?cid N) (eq ?cid n))))
    ?recommender <- (course_recommender (Duration ?d&: (not (eq ?d nil))) (Decision ?dec&: (eq ?dec nil)))
    =>
    (recommend_course_seq ?user ?recommender ?user.CurrentDomain)
)

(defrule cs_no_change_action
    ?user <- (user_profile (CurrentDomain ?cd&: (eq ?cd Computer_Science)) (ChangeInDomain ?cid&: (or (eq ?cid N) (eq ?cid n))))
    ?recommender <- (course_recommender (Duration ?d&: (not (eq ?d nil))) (Decision ?dec&: (eq ?dec nil)))
    =>
    (recommend_course_seq ?user ?recommender ?user.CurrentDomain)
)

(defrule math_ds_transition_rule_1
    ?user <- (user_profile (CurrentDomain ?cd&: (eq ?cd Math)) (Skills ?s&: (eq Logic ?s)))
    ?recommender <- (course_recommender (RecommendedDomain ?rd&: (eq ?rd Data_Science)) (Decision ?dec&: (eq ?dec nil)) )
    =>
    (assign_course ?user ?recommender Probability_And_Statistics)
)

(defrule math_ds_transition_rule_2
    ?user <- (user_profile (CurrentDomain ?cd&: (eq ?cd Math)) (Skills ?s&: (eq Statistics ?s)))
    ?recommender <- (course_recommender (RecommendedDomain ?rd&: (eq ?rd Data_Science)) (Decision ?dec&: (eq ?dec nil)) )
    =>
    (assign_course ?user ?recommender Data_Analysis)
)

(defrule math_ds_transition_rule_3
    ?user <- (user_profile (CurrentDomain ?cd&: (eq ?cd Math)) (MultipleSkills ?ms&: (eq ?ms True)))
    ?recommender <- (course_recommender (RecommendedDomain ?rd&: (eq ?rd Data_Science)) (Decision ?dec&: (eq ?dec nil)) )
    =>
    (check_assign_course ?user ?recommender Machine_Learning Logic Statistics)
)

(defrule ds_cs_transition_rule_1
    ?user <- (user_profile (CurrentDomain ?cd&: (eq ?cd Data_Science)) (Skills ?s&: (eq Data_Analysis ?s)))
    ?recommender <- (course_recommender (RecommendedDomain ?rd&: (eq ?rd Computer_Science)) (Decision ?dec&: (eq ?dec nil)) )
    =>
    (assign_course ?user ?recommender Algorithms)
    (assign_course ?user ?recommender Design)
)

(defrule ds_cs_transition_rule_2
    ?user <- (user_profile (CurrentDomain ?cd&: (eq ?cd Data_Science)) (Skills ?s&: (eq Machine_Learning ?s)))
    ?recommender <- (course_recommender (RecommendedDomain ?rd&: (eq ?rd Computer_Science)) (Decision ?dec&: (eq ?dec nil)) )
    =>
    (assign_course ?user ?recommender Software_development)
    (assign_course ?user ?recommender Web_development)
)

(defrule ds_cs_transition_rule_3
    ?user <- (user_profile (CurrentDomain ?cd&: (eq ?cd Data_Science)) (Skills ?s&: (eq Probability_And_Statistics ?s)))
    ?recommender <- (course_recommender (RecommendedDomain ?rd&: (eq ?rd Computer_Science)) (Decision ?dec&: (eq ?dec nil)) )
    =>
    (assign_course ?user ?recommender Algorithms)
)

(defrule ds_cs_transition_rule_4
    ?user <- (user_profile (CurrentDomain ?cd&: (eq ?cd Data_Science)) (MultipleSkills ?ms&: (eq ?ms True)))
    ?recommender <- (course_recommender (RecommendedDomain ?rd&: (eq ?rd Computer_Science)) (Decision ?dec&: (eq ?dec nil)) )
    =>
    (check_assign_course ?user ?recommender Design Machine_Learning Data_Analysis Probability_And_Statistics)
    (check_assign_course ?user ?recommender Web_development Machine_Learning Data_Analysis)
    (check_assign_course ?user ?recommender Algorithms Data_Analysis Probability_And_Statistics)
)

(defrule cs_ds_transition_rule_1
    ?user <- (user_profile (CurrentDomain ?cd&: (eq ?cd Computer_Science)) (MultipleSkills ?ms&: (not (eq ?ms True))))
    ?recommender <- (course_recommender (RecommendedDomain ?rd&: (eq ?rd Data_Science)) (Decision ?dec&: (eq ?dec nil)) )
    =>
    (assign_course ?user ?recommender Probability_And_Statistics)
)

(defrule cs_ds_transition_rule_2
    ?user <- (user_profile (CurrentDomain ?cd&: (eq ?cd Computer_Science)) (MultipleSkills ?ms&: (eq ?ms True)))
    ?recommender <- (course_recommender (RecommendedDomain ?rd&: (eq ?rd Data_Science)) (Decision ?dec&: (eq ?dec nil)) )
    =>
    (check_assign_course ?user ?recommender Machine_Learning Algorithms Design)
    (check_assign_course ?user ?recommender Data_Analysis Algorithms Web_development)
    (assign_course ?user ?recommender Machine_Learning)
)

(defrule ds_recommend_chain_1
    ?recommender <- (course_recommender (RecommendedDomain ?rd&: (eq ?rd Data_Science)) (Decision ?dec&: (eq ?dec Maybe)) (RecommendedCourses ?rc&: (eq ?rc Data_Analysis )))
    ?user <- (user_profile (Name ?n&: (not (eq ?n nil))))
    =>
    (assign_course ?user ?recommender Machine_Learning)
)

(defrule ds_recommend_chain_2
    ?recommender <- (course_recommender (RecommendedDomain ?rd&: (eq ?rd Data_Science)) (Decision ?dec&: (eq ?dec Maybe)) (RecommendedCourses ?rc&: (eq ?rc Probability_And_Statistics )))
    ?user <- (user_profile (Name ?n&: (not (eq ?n nil))))
    =>
    (assign_course ?user ?recommender Data_Analysis)
)

(defrule ds_recommend_chain_3
    ?recommender <- (course_recommender (RecommendedDomain ?rd&: (eq ?rd Data_Science)) (Decision ?dec&: (eq ?dec Maybe)) (RecommendedCourses ?rc&: (eq ?rc Machine_Learning )))
    ?user <- (user_profile (Name ?n&: (not (eq ?n nil))))
    =>
    (assign_course ?user ?recommender Data_Analysis)
)

(defrule ds_recommend_chain_4
    ?recommender <- (course_recommender (RecommendedDomain ?rd&: (eq ?rd Data_Science)) (Decision ?dec&: (eq ?dec Maybe)) (RecommendedCourses $? Probability_And_Statistics Data_Analysis $?))
    ?user <- (user_profile (Name ?n&: (not (eq ?n nil))))
    =>
    (assign_course ?user ?recommender Machine_Learning)
    (modify ?recommender (Decision Yes))
)

(defrule cs_recommend_chain_1
    ?recommender <- (course_recommender (RecommendedDomain ?rd&: (eq ?rd Computer_Science)) (Decision ?dec&: (eq ?dec Maybe)) (RecommendedCourses ?rc&: (eq ?rc Algorithms )))
    ?user <- (user_profile (Name ?n&: (not (eq ?n nil))))
    =>
    (assign_course ?user ?recommender Software_development)
)

(defrule cs_recommend_chain_2
    ?recommender <- (course_recommender (RecommendedDomain ?rd&: (eq ?rd Computer_Science)) (Decision ?dec&: (eq ?dec Maybe)) (RecommendedCourses ?rc&: (eq ?rc Design )))
    ?user <- (user_profile (Name ?n&: (not (eq ?n nil))))
    =>
    (assign_course ?user ?recommender Web_development)
)

(defrule cs_recommend_chain_3
    ?recommender <- (course_recommender (RecommendedDomain ?rd&: (eq ?rd Computer_Science)) (Decision ?dec&: (eq ?dec Maybe)) (RecommendedCourses ?rc&: (eq ?rc Web_development )))
    ?user <- (user_profile (Name ?n&: (not (eq ?n nil))))
    =>
    (assign_course ?user ?recommender Mobile_development)
)

(defrule cs_recommend_chain_4
    ?recommender <- (course_recommender (RecommendedDomain ?rd&: (eq ?rd Computer_Science)) (Decision ?dec&: (eq ?dec Maybe)) (RecommendedCourses ?rc&: (eq ?rc Software_development )))
    ?user <- (user_profile (Name ?n&: (not (eq ?n nil))))
    =>
    (assign_course ?user ?recommender Mobile_development)
)

(defrule cs_recommend_chain_5
    ?recommender <- (course_recommender (RecommendedDomain ?rd&: (eq ?rd Computer_Science)) (Decision ?dec&: (eq ?dec Maybe)) (RecommendedCourses ?rc&: (eq ?rc Mobile_development )))
    ?user <- (user_profile (Name ?n&: (not (eq ?n nil))))
    =>
    (assign_course ?user ?recommender Web_development)
)

(defrule cs_recommend_chain_6
    ?recommender <- (course_recommender (RecommendedDomain ?rd&: (eq ?rd Computer_Science)) (Decision ?dec&: (eq ?dec Maybe)) (RecommendedCourses $? Algorithms Software_development $?))
    ?user <- (user_profile (Name ?n&: (not (eq ?n nil))))
    =>
    (assign_course ?user ?recommender Web_development)
    (modify ?recommender (Decision Yes))
)

(defrule cs_recommend_chain_7
    ?recommender <- (course_recommender (RecommendedDomain ?rd&: (eq ?rd Computer_Science)) (Decision ?dec&: (eq ?dec Maybe)) (RecommendedCourses $? Algorithms Design $?))
    ?user <- (user_profile (Name ?n&: (not (eq ?n nil))))
    =>
    (assign_course ?user ?recommender Software_development)
    (modify ?recommender (Decision Yes))
)

(defrule cs_recommend_chain_8
    ?recommender <- (course_recommender (RecommendedDomain ?rd&: (eq ?rd Computer_Science)) (Decision ?dec&: (eq ?dec Maybe)) (RecommendedCourses $? Software_development Web_development $?))
    ?user <- (user_profile (Name ?n&: (not (eq ?n nil))))
    =>
    (assign_course ?user ?recommender Mobile_development)
    (modify ?recommender (Decision Yes))
)

;;Main control flow;;
(fetchUserProfile)
(run)
(displayRecommendations)
