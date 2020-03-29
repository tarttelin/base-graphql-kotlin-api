
CREATE TABLE IF NOT EXISTS community (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(250)
);

CREATE TABLE IF NOT EXISTS household (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nameOrNumber VARCHAR(250),
    postcode VARCHAR(250),
    communityId INT
);

CREATE TABLE IF NOT EXISTS member (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(250),
    householdId INT,
    userId VARCHAR(250)
);

CREATE TABLE IF NOT EXISTS thing (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(250),
    category VARCHAR(250),
    memberId INT
);

ALTER TABLE household
    ADD FOREIGN KEY (communityId)
    REFERENCES community(id);

ALTER TABLE member
    ADD FOREIGN KEY (householdId)
    REFERENCES household(id);

ALTER TABLE thing
    ADD FOREIGN KEY (memberId)
    REFERENCES member(id);
