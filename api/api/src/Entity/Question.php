<?php

namespace App\Entity;

use ApiPlatform\Core\Annotation\ApiResource;
use App\Repository\QuestionRepository;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: QuestionRepository::class)]
#[ApiResource]
class Question
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: 'integer')]
    private $id;

    #[ORM\Column(type: 'string', length: 255)]
    private $content;

    #[ORM\Column(type: 'string', length: 255)]
    private $answer1;

    #[ORM\Column(type: 'string', length: 255)]
    private $answer2;

    #[ORM\Column(type: 'string', length: 255)]
    private $answer3;

    #[ORM\Column(type: 'string', length: 255)]
    private $answer4;

    #[ORM\Column(type: 'smallint')]
    private $answerIndex;

    #[ORM\ManyToOne(targetEntity: User::class, inversedBy: 'createdQuestions')]
    #[ORM\JoinColumn(nullable: false)]
    private $creator;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getContent(): ?string
    {
        return $this->content;
    }

    public function setContent(string $content): self
    {
        $this->content = $content;

        return $this;
    }

    public function getAnswer1(): ?string
    {
        return $this->answer1;
    }

    public function setAnswer1(string $answer1): self
    {
        $this->answer1 = $answer1;

        return $this;
    }

    public function getAnswer2(): ?string
    {
        return $this->answer2;
    }

    public function setAnswer2(string $answer2): self
    {
        $this->answer2 = $answer2;

        return $this;
    }

    public function getAnswer3(): ?string
    {
        return $this->answer3;
    }

    public function setAnswer3(string $answer3): self
    {
        $this->answer3 = $answer3;

        return $this;
    }

    public function getAnswer4(): ?string
    {
        return $this->answer4;
    }

    public function setAnswer4(string $answer4): self
    {
        $this->answer4 = $answer4;

        return $this;
    }

    public function getAnswerIndex(): ?int
    {
        return $this->answerIndex;
    }

    public function setAnswerIndex(int $answerIndex): self
    {
        $this->answerIndex = $answerIndex;

        return $this;
    }

    public function getCreator(): ?User
    {
        return $this->creator;
    }

    public function setCreator(?User $creator): self
    {
        $this->creator = $creator;

        return $this;
    }
}
