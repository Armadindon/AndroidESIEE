<?php

namespace App\Entity;

use ApiPlatform\Core\Annotation\ApiResource;
use App\Repository\ScoreRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Serializer\Annotation\Groups;

#[ORM\Entity(repositoryClass: ScoreRepository::class)]
#[ApiResource(
    normalizationContext: ["groups" => ["user:read"]],
    denormalizationContext: ["groups" => ["user:write"]],
)]
class Score
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: 'integer')]
    private $id;

    #[ORM\Column(type: 'smallint')]
    #[Groups(["user:read","user:write"])]
    private $score;

    #[ORM\ManyToOne(targetEntity: User::class, inversedBy: 'scores')]
    #[ORM\JoinColumn(nullable: false)]
    #[Groups(["user:read","user:write"])]
    private $byUser;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getScore(): ?int
    {
        return $this->score;
    }

    public function setScore(int $score): self
    {
        $this->score = $score;

        return $this;
    }

    public function getByUser(): ?User
    {
        return $this->byUser;
    }

    public function setByUser(?User $byUser): self
    {
        $this->byUser = $byUser;

        return $this;
    }
}
