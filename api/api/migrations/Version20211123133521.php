<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20211123133521 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE SEQUENCE score_id_seq INCREMENT BY 1 MINVALUE 1 START 1');
        $this->addSql('CREATE TABLE score (id INT NOT NULL, by_user_id INT NOT NULL, score SMALLINT NOT NULL, PRIMARY KEY(id))');
        $this->addSql('CREATE INDEX IDX_32993751DC9C2434 ON score (by_user_id)');
        $this->addSql('ALTER TABLE score ADD CONSTRAINT FK_32993751DC9C2434 FOREIGN KEY (by_user_id) REFERENCES "user" (id) NOT DEFERRABLE INITIALLY IMMEDIATE');
        $this->addSql('ALTER TABLE question DROP CONSTRAINT fk_b6f7494eb03a8386');
        $this->addSql('DROP INDEX idx_b6f7494eb03a8386');
        $this->addSql('ALTER TABLE question ADD answer_index SMALLINT NOT NULL');
        $this->addSql('ALTER TABLE question DROP created_by_id');
        $this->addSql('ALTER TABLE question RENAME COLUMN correct_answer TO creator_id');
        $this->addSql('ALTER TABLE question RENAME COLUMN question TO content');
        $this->addSql('ALTER TABLE question ADD CONSTRAINT FK_B6F7494E61220EA6 FOREIGN KEY (creator_id) REFERENCES "user" (id) NOT DEFERRABLE INITIALLY IMMEDIATE');
        $this->addSql('CREATE INDEX IDX_B6F7494E61220EA6 ON question (creator_id)');
        $this->addSql('ALTER TABLE "user" DROP score');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE SCHEMA public');
        $this->addSql('DROP SEQUENCE score_id_seq CASCADE');
        $this->addSql('DROP TABLE score');
        $this->addSql('ALTER TABLE "user" ADD score INT NOT NULL');
        $this->addSql('ALTER TABLE question DROP CONSTRAINT FK_B6F7494E61220EA6');
        $this->addSql('DROP INDEX IDX_B6F7494E61220EA6');
        $this->addSql('ALTER TABLE question ADD created_by_id INT DEFAULT NULL');
        $this->addSql('ALTER TABLE question DROP answer_index');
        $this->addSql('ALTER TABLE question RENAME COLUMN content TO question');
        $this->addSql('ALTER TABLE question RENAME COLUMN creator_id TO correct_answer');
        $this->addSql('ALTER TABLE question ADD CONSTRAINT fk_b6f7494eb03a8386 FOREIGN KEY (created_by_id) REFERENCES "user" (id) NOT DEFERRABLE INITIALLY IMMEDIATE');
        $this->addSql('CREATE INDEX idx_b6f7494eb03a8386 ON question (created_by_id)');
    }
}
