<?php

namespace App\Entity;

use ApiPlatform\Core\Annotation\ApiFilter;
use App\Repository\UserRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Security\Core\User\PasswordAuthenticatedUserInterface;
use Symfony\Component\Security\Core\User\UserInterface;
use ApiPlatform\Core\Annotation\ApiResource;
use Symfony\Component\Serializer\Annotation\Groups;
use Symfony\Component\Serializer\Annotation\SerializedName;
use ApiPlatform\Core\Bridge\Doctrine\Orm\Filter\SearchFilter;

#[ORM\Entity(repositoryClass: UserRepository::class)]
#[ORM\Table(name: '`user`')]
#[ApiResource(
    normalizationContext: ["groups" => ["user:read"]],
    denormalizationContext: ["groups" => ["user:write"]],
)]
#[ApiFilter(SearchFilter::class, properties:['username' => 'exact'])]
class User implements UserInterface, PasswordAuthenticatedUserInterface
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: 'integer')]
    private $id;

    #[ORM\Column(type: 'string', length: 180, unique: true)]
    #[Groups(["user:read","user:write"])]
    private $username;

    #[ORM\Column(type: 'json')]
    private $roles = [];

    #[ORM\Column(type: 'string')]
    private $password;

    #[ORM\Column(type: 'string', length: 255)]
    #[Groups(["user:read","user:write"])]
    private $firstname;

    #[ORM\Column(type: 'string', length: 255)]
    #[Groups(["user:read","user:write"])]
    private $lastname;

    #[ORM\OneToMany(mappedBy: 'byUser', targetEntity: Score::class, orphanRemoval: true)]
    private $scores;

    #[ORM\OneToMany(mappedBy: 'creator', targetEntity: Question::class)]
    private $createdQuestions;

    #[Groups(["user:write"])]
    #[SerializedName("password")]
    private $plainPassword;

    public function __construct()
    {
        $this->scores = new ArrayCollection();
        $this->createdQuestions = new ArrayCollection();
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    /**
     * @deprecated since Symfony 5.3, use getUserIdentifier instead
     */
    public function getUsername(): string
    {
        return (string) $this->username;
    }

    public function setUsername(string $username): self
    {
        $this->username = $username;

        return $this;
    }

    /**
     * A visual identifier that represents this user.
     *
     * @see UserInterface
     */
    public function getUserIdentifier(): string
    {
        return (string) $this->username;
    }

    /**
     * @see UserInterface
     */
    public function getRoles(): array
    {
        $roles = $this->roles;
        // guarantee every user at least has ROLE_USER
        $roles[] = 'ROLE_USER';

        return array_unique($roles);
    }

    public function setRoles(array $roles): self
    {
        $this->roles = $roles;

        return $this;
    }

    /**
     * @see PasswordAuthenticatedUserInterface
     */
    public function getPassword(): string
    {
        return $this->password;
    }

    public function setPassword(string $password): self
    {
        $this->password = $password;

        return $this;
    }

    /**
     * Returning a salt is only needed, if you are not using a modern
     * hashing algorithm (e.g. bcrypt or sodium) in your security.yaml.
     *
     * @see UserInterface
     */
    public function getSalt(): ?string
    {
        return null;
    }

    /**
     * @see UserInterface
     */
    public function eraseCredentials()
    {
        $this->plainPassword = null;
    }

    public function getFirstname(): ?string
    {
        return $this->firstname;
    }

    public function setFirstname(string $firstname): self
    {
        $this->firstname = $firstname;

        return $this;
    }

    public function getLastname(): ?string
    {
        return $this->lastname;
    }

    public function setLastname(string $lastname): self
    {
        $this->lastname = $lastname;

        return $this;
    }

    /**
     * @return Collection|Score[]
     */
    public function getScores(): Collection
    {
        return $this->scores;
    }

    public function addScore(Score $score): self
    {
        if (!$this->scores->contains($score)) {
            $this->scores[] = $score;
            $score->setByUser($this);
        }

        return $this;
    }

    public function removeScore(Score $score): self
    {
        if ($this->scores->removeElement($score)) {
            // set the owning side to null (unless already changed)
            if ($score->getByUser() === $this) {
                $score->setByUser(null);
            }
        }

        return $this;
    }

    /**
     * @return Collection|Question[]
     */
    public function getCreatedQuestions(): Collection
    {
        return $this->createdQuestions;
    }

    public function addCreatedQuestion(Question $createdQuestion): self
    {
        if (!$this->createdQuestions->contains($createdQuestion)) {
            $this->createdQuestions[] = $createdQuestion;
            $createdQuestion->setCreator($this);
        }

        return $this;
    }

    public function removeCreatedQuestion(Question $createdQuestion): self
    {
        if ($this->createdQuestions->removeElement($createdQuestion)) {
            // set the owning side to null (unless already changed)
            if ($createdQuestion->getCreator() === $this) {
                $createdQuestion->setCreator(null);
            }
        }

        return $this;
    }

    public function getPlainPassword(): ?string
    {
        return $this->plainPassword;
    }

    public function setPlainPassword(?string $plainPassword): self
    {
        $this->plainPassword = $plainPassword;

        return $this;
    }
}
